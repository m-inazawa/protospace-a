// 状態管理（FOLLOW, SLEEP等// 右クリックメニュー、エフェクト表示

export const STATES = {
  FOLLOW: 'follow',
  DRAG: 'drag',
  SLEEP: 'sleep',
  IDLE: 'idle',
  RUSH: 'rush',
  ROLL: 'roll'
};

export class StateManager {
  constructor() {
    this.currentState = STATES.FOLLOW;
    this.lastActionTime = Date.now();
    this.idleThreshold = 5000; // 5秒間動かなければIDLE/SLEEPへ
  }

  // 毎フレーム Mascot.js から呼ばれる
  update(isDragging, isMoving) {
    const now = Date.now();

    if (isDragging) {
      this.currentState = STATES.DRAG;
      this.lastActionTime = now;
      return this.currentState;
    }
    
    if (isMoving) {
      // 外部から RUSH 状態にされている場合はそれを優先
      if (this.currentState !== STATES.RUSH) {
          this.currentState = STATES.FOLLOW;
      }
      this.lastActionTime = now;
      return this.currentState;
    }

    // 3. 静止中・放置判定
    const idleDuration = now - this.lastActionTime;

    if (idleDuration > this.idleThreshold) {
      // すでに特別な待機モーション（SLEEP, ROLL）に入っていなければランダムで決定
      if (this.currentState !== STATES.SLEEP && this.currentState !== STATES.ROLL) {
        const rand = Math.random();
        this.currentState = rand > 0.5 ? STATES.SLEEP : STATES.ROLL;
      }
    } else {
      // 5秒未満の停止は通常の待機
      this.currentState = STATES.IDLE;
    }

    return this.currentState;
  }

  // 外部から強制的に状態を変えたい場合（餌を置いた時など）
  setState(state) {
    // STATES に定義されている値か確認するガードを入れるとより安全です
    if (Object.values(STATES).includes(state)) {
      this.currentState = state;
      this.lastActionTime = Date.now();
    }
  }

}