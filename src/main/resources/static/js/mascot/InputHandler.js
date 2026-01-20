// マウス、ドラッグ、右クリック監視

export class InputHandler {
    constructor() {
        this.mouseX = 0;
        this.mouseY = 0;
        this.isDragging = false;

        // マウス移動を監視
        window.addEventListener('mousemove', (e) => {
            this.mouseX = e.clientX;
            this.mouseY = e.clientY;
        });

        // ドラッグ開始（キャラクターの上でマウスが押された時）
        // ※後ほどMascot.jsから呼び出すか、ここでイベント登録します
    }

    getMousePos() {
        return { x: this.mouseX, y: this.mouseY };
    }
}