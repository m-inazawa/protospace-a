// エントリーポイント（初期化処理）

import { Mascot } from './mascot/Mascot.js';

// グローバルからアクセスできるようにappとして定義（メニュー操作用）
window.app = new Mascot();

// ページ読み込み完了後に初期化
window.addEventListener('DOMContentLoaded', () => {
    window.app.init();
});