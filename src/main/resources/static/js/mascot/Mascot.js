// 司令塔（全体制御
//メインループ（requestAnimationFrame）を回し、キャラクターを動かし続けるクラスです。

import { InputHandler } from '/js/mascot/InputHandler.js';
import { Utils } from '/js/mascot/Utils.js';
import { CharacterConfigs } from '/js/mascot/MascotEntity.js';
import { StateManager } from '/js/mascot/StateManager.js';

export class Mascot {
  constructor() {
    // --- DOM要素の取得 ---
    this.el = document.getElementById('mascot');
    this.sprite = document.getElementById('mascot-sprite');

    // --- 設定の読み込み ---
    this.config = CharacterConfigs.CAT;
        
    // --- 状態・アニメーション管理 ---
    this.stateManager = new StateManager();
    this.frame = 1;      // 現在のコマ番号 (1〜4)
    this.lastFrameTime = 0; // 前回のコマ更新時刻
    this.currentDir = 's'; // 初期向き：南(下)
    this.isMoving = false;

    // --- 座標管理 ---
    this.x = window.innerWidth / 2;
    this.y = window.innerHeight / 2;
    this.targetX = this.x;
    this.targetY = this.y;
    this.speed = 0.08;

    // --- 入力ハンドラのインスタンス化 ---
    this.input = new InputHandler(this.el); 
  }

  init() {
    console.log("Mascot system initialized.");
    this.preloadImages(); // 画像の事前読み込み
    this.loop();
  }

  // 画像のチラつきを防止
  preloadImages() {
    const directions = ['n', 'ne', 'e', 'se', 's', 'sw', 'w', 'nw'];
    directions.forEach(dir => {
      for (let i = 1; i <= this.config.frameCount; i++) {
        const img = new Image();

        // 向き(dir)を1文字目だけ大文字にする（例: s -> S）
        const dirKey = dir.toUpperCase();

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
    } else {
      this.targetX = mouse.x;
      this.targetY = mouse.y;

      const dx = this.targetX - this.x;
      const dy = this.targetY - this.y;

      // 距離を計算して移動中か判定
      const distance = Utils.getDistance(this.x, this.y, this.targetX, this.targetY);
      this.isMoving = distance > 2; // 2px以上差があれば移動中

      if (this.isMoving) {
        // 向きを更新
        const nextDir = Utils.getDirection8(dx, dy);
        if (nextDir) this.currentDir = nextDir;

        // 移動
        this.x += dx * this.speed;
        this.y += dy * this.speed;
      }
    }

    // 状態の更新 (StateManagerに今の状況を伝える)
    this.stateManager.update(this.input.isDragging, this.isMoving);

    // アニメーションのコマ更新
    const now = Date.now();
    if (this.isMoving && now - this.lastFrameTime > this.config.interval) {
      this.frame = (this.frame % this.config.frameCount) + 1;
      this.lastFrameTime = now;
    } else if (!this.isMoving) {
      this.frame = 1;
    }
  }

  draw() {
    // 位置　CSSのtransformを使用して描画（パフォーマンスが良い）
    this.el.style.transform = `translate(${this.x}px, ${this.y}px)`;

    // 1. 向き(currentDir)を大文字に変換（例: 's' -> 'S'）
    const dirKey = this.currentDir.toUpperCase();

    // 2. ファイル名を組み立てる（例: catS1.png）
    // 状態(state)と向き(dir)とコマ(frame)から画像パスを生成
    // 例: /images/characters/cat/sw-2.png
    const fileName = `cat${this.currentDir}${this.frame}.png`;
    this.sprite.style.backgroundImage = `url('${this.config.basePath}${fileName}')`;

    // 3. パスを結合
    const fullPath = `${this.config.basePath}${fileName}`;
    this.sprite.style.backgroundImage = `url('${fullPath}')`;
        
    // CSSクラスの適用 (状態管理用)
    // ドラッグ中などの状態クラスも一応維持
    this.sprite.className = `mascot-sprite state-${this.stateManager.currentState}`;

    // カーソルスタイル　ドラッグ中の見た目変更
    this.el.style.cursor = this.input.isDragging ? 'grabbing' : 'grab';
  }
}