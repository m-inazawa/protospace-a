// 共通計算（8方向判定、距離計算）
export const Utils = {
  // 2点間の距離を計算
  getDistance: function(x1, y1, x2, y2) {
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  },

  // 角度から8方向のクラス名を返す
  getDirection8: (dx, dy) => {
    if (Math.abs(dx) < 5 && Math.abs(dy) < 5) return ''; // 動いていない時は更新しない

    const angle = Math.atan2(dy, dx) * (180 / Math.PI);
    const directions = ['e', 'se', 's', 'sw', 'w', 'nw', 'n', 'ne'];
    // 角度を0〜360の範囲に変換して、45度ずつで割る
    const index = Math.round(((angle < 0 ? angle + 360 : angle) % 360) / 45) % 8;
    return directions[index];
  }
};