// 状態管理（FOLLOW, SLEEP等// 右クリックメニュー、エフェクト表示

export const STATES = {
  FOLLOW: 'follow',
  DRAG: 'drag',
  SLEEP: 'sleep',
  IDLE: 'idle',
  RUSH: 'rush'
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
    } else if (isMoving) {
      this.currentState = STATES.FOLLOW;
      this.lastActionTime = now;
    } else {
      // 放置判定
      if (now - this.lastActionTime > this.idleThreshold) {
      // 将来的にはここでランダムにSLEEPかIDLE(ごろごろ)を選択
      this.currentState = STATES.SLEEP;
      } else {
        this.currentState = STATES.IDLE;
      }
    }
      return this.currentState;
    }

    // 外部から強制的に状態を変えたい場合（餌を置いた時など）
  setState(state) {
    this.currentState = state;
    this.lastActionTime = Date.now();
  }
}