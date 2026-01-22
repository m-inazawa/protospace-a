import { InputHandler } from '/js/mascot/InputHandler.js';
import { Utils } from '/js/mascot/Utils.js';
import { CharacterConfigs } from '/js/mascot/MascotEntity.js';
import { StateManager } from '/js/mascot/StateManager.js';
import { UIHandler } from '/js/mascot/UIHandler.js';

export class Mascot {
  constructor() {
    this.el = document.getElementById('mascot');
    this.sprite = document.getElementById('mascot-sprite');
    this.isAnimationEnabled = true;

    if (this.sprite) {
      console.log("SUCCESS: mascot-sprite element found!");
    }

    this.config = CharacterConfigs.CAT;
    this.stateManager = new StateManager();
    this.frame = 1;
    this.lastFrameTime = 0;
    this.currentDir = 's';
    this.isMoving = false;

    this.x = window.innerWidth / 2;
    this.y = window.innerHeight / 2;
    this.targetX = this.x;
    this.targetY = this.y;
    this.speed = 0.006;

    this.input = new InputHandler(this.el); 

    this.ui = new UIHandler(this);
    this.foodPos = null;

    this.jumpY = 0;       // ジャンプによる高さオフセット
    this.gravity = 0.8;   // 重力
    this.jumpPower = -12; // 跳ねる力
    this.isJumping = false;
  }

  init() {
    this.preloadImages();
    this.loop();
  }

  preloadImages() {
    const directions = ['n', 'ne', 'e', 'se', 's', 'sw', 'w', 'nw'];
    directions.forEach(dir => {
      // フォルダ内の実際の数(config.frames[dir])か、デフォルト4枚
      const count = (this.config.frames && this.config.frames[dir]) || 4;
      for (let i = 1; i <= count; i++) {
        const img = new Image();
        const dirKey = dir.charAt(0).toUpperCase() + dir.slice(1);
        img.src = `${this.config.basePath}cat${dirKey}${i}.png`;
      }
    });
  }

  loop() {
    this.update();
    this.draw();
    requestAnimationFrame(() => this.loop());
  }

  update() {
    const mouse = this.input.getMousePos();

    if (this.input.isDragging) {
      this.x = mouse.x;
      this.y = mouse.y;
      this.isMoving = false;
      this.foodPos = null; // 掴まれたら餌を諦める
    } else {
      // 餌があれば餌を優先、なければマウスを追う
      this.targetX = this.foodPos ? this.foodPos.x : mouse.x;
      this.targetY = this.foodPos ? this.foodPos.y : mouse.y;

      const dx = this.targetX - this.x;
      const dy = this.targetY - this.y;

      const distance = Utils.getDistance(this.x, this.y, this.targetX, this.targetY);
      this.isMoving = distance > 5;

      if (this.isMoving) {
        const nextDir = Utils.getDirection8(dx, dy);
        if (nextDir) this.currentDir = nextDir;
        this.x += dx * this.speed;
        this.y += dy * this.speed;
      } else if (this.foodPos) {
        // 餌に到着した場合
        this.eatFood();
      }
    }

      // ジャンプの物理計算
    if (this.isJumping) {
      this.jumpY += this.jumpVelocity;
      this.jumpVelocity += this.gravity;

      if (this.jumpY >= 0) { // 着地判定
        this.jumpY = 0;
        this.isJumping = false;
      }
    }

    

    this.stateManager.update(this.input.isDragging, this.isMoving);

    // アニメーション更新
    const now = Date.now();
    const maxFrames = (this.config.frames && this.config.frames[this.currentDir]) || 2;
    if (this.isMoving && now - this.lastFrameTime > this.config.interval) {
      this.frame = (this.frame % maxFrames) + 1;
      this.lastFrameTime = now;
    } else if (!this.isMoving) {
      this.frame = 1;
    }
  }

  setTargetFood(x, y) {
    this.foodPos = { x, y };
  }

  eatFood() {
    this.foodPos = null;
    const foodEl = document.getElementById('cat-food');
    if (foodEl) foodEl.remove();
    
    // 食べた後に眠る状態へ（StateManagerに任せる）
    this.stateManager.setState('sleep');
  }

  jump() {
    if (!this.isJumping) {
        this.isJumping = true;
        this.jumpVelocity = this.jumpPower;
        this.stateManager.setState('idle'); // 状態を一時的にリセット
    }
  }

  // 方角を更新する関数の中のイメージ
  updateDirection(newDir) {
    const container = document.querySelector('.mascot-container');
    
    // 1. 全ての方角クラス（n, s, e, w, ne, nw, se, sw）を一旦削除
    const allDirs = ['n', 's', 'e', 'w', 'ne', 'nw', 'se', 'sw'];
    container.classList.remove(...allDirs.map(d => `dir-${d}`));

    // 2. 新しい方角クラスを追加
    container.classList.add(`dir-${newDir}`);
  }

  draw() {
    // 1. 位置の更新（中心を基準にするため translate の入れ子に注意）
    // CSSで -50% しているので、ここは単純に座標を指定するだけでOK
    this.el.style.left = `${this.x}px`;
    this.el.style.top = `${this.y}px`;

    // 2．ファイル名の組み立て（例: ne -> Ne, s -> S）
    const dirKey = this.currentDir.charAt(0).toUpperCase() + this.currentDir.slice(1);
    const fileName = `cat${dirKey}${this.frame}.png`;
    const fullPath = `${this.config.basePath}${fileName}`;

    // 3．背景画像の設定（!important をプロパティとして設定）
    this.sprite.style.setProperty('background-image', `url('${fullPath}')`, 'important');
    
    // 4. 【重要】コンテナ(this.el)に方角クラスを付与（これでCSSのサイズが変わる）
    // 既存の方角クラスを削除してから新しいクラスを追加
    const allDirs = ['n', 's', 'e', 'w', 'ne', 'nw', 'se', 'sw'];
    this.el.classList.remove(...allDirs.map(d => `dir-${d}`));
    this.el.classList.add(`dir-${this.currentDir}`);

    // 5. スプライト(this.sprite)に状態クラスを付与
    // 状態と向きをクラスに反映
    this.sprite.className = `mascot-sprite state-${this.stateManager.currentState}`;
    this.el.style.cursor = this.input.isDragging ? 'grabbing' : 'grab';
  }

  changeCharacter(charKey) {
    const newConfig = CharacterConfigs[charKey];
    if (newConfig) {
      this.config = newConfig;
      this.preloadImages(); // 新しいキャラの画像をプリロード
      this.frame = 1;
      this.speed = newConfig.speed;
      console.log(`Changed to ${newConfig.name}`);
    }
  }
}