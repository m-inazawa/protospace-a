// エントリーポイント（初期化処理）

import { Mascot } from '/js/mascot/Mascot.js';

// ページ読み込み完了後に初期化
window.addEventListener('DOMContentLoaded', () => {
  const app = new Mascot();
  app.init();
  // グローバルからアクセスできるようにappとして定義して公開する（メニュー操作用）
  window.app = app;
});