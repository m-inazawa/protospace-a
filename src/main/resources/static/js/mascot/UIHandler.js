// 右クリックメニューと餌(Food)の生成

export class UIHandler {
  constructor(mascot) {
    this.mascot = mascot;
    this.menu = document.getElementById('custom-menu');
    this.initEvents();
  }

  initEvents() {
    // 右クリックメニューの表示
    window.addEventListener('contextmenu', (e) => {
      e.preventDefault();
      this.menu.style.display = 'block';
      this.menu.style.left = `${e.clientX}px`;
      this.menu.style.top = `${e.clientY}px`;
    });

    // メニュー以外をクリックしたら閉じる
    window.addEventListener('click', () => {
      this.menu.style.display = 'none';
    });
  }

  // index.htmlの onclick="app.ui.placeFoodAtMenu()" から呼ばれる想定
  placeFoodAtMenu() {
    const x = parseInt(this.menu.style.left);
    const y = parseInt(this.menu.style.top);
    
    // 既存の餌があれば削除
    const oldFood = document.getElementById('cat-food');
    if (oldFood) oldFood.remove();

    // 餌のDOMを生成
    const food = document.createElement('div');
    food.id = 'cat-food';
    food.className = 'food-item';
    food.style.left = `${x}px`;
    food.style.top = `${y}px`;
    document.body.appendChild(food);

    // 猫に餌の場所を知らせる
    this.mascot.setTargetFood(x, y);
  }
}