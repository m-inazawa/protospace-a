// 司令塔（全体制御
//メインループ（requestAnimationFrame）を回し、キャラクターを動かし続けるクラスです。

import { InputHandler } from './InputHandler.js';
import { Utils } from './Utils.js';

export class Mascot {
    constructor() {
        this.el = document.getElementById('mascot');
        this.sprite = document.getElementById('mascot-sprite');
        this.input = new InputHandler();
        
        // 座標管理
        this.x = 100;
        this.y = 100;
        this.targetX = 100;
        this.targetY = 100;
        
        this.speed = 0.05; // 追従の滑らかさ (0.1 = 10%ずつ近づく)
    }

    init() {
        console.log("Mascot system initialized.");
        this.loop();
    }

    // メインループ（1秒間に60回実行を目指す）
    loop() {
        this.update();
        this.draw();
        requestAnimationFrame(() => this.loop());
    }

    update() {
        // InputHandlerからマウス座標を取得
        const mouse = this.input.getMousePos();
        
        // ドラッグ中でなければ、マウスを目標地点にする
        if (!this.input.isDragging) {
            this.targetX = mouse.x;
            this.targetY = mouse.y;
        }

        // 線形補完(lerp)で滑らかに移動
        this.x += (this.targetX - this.x) * this.speed;
        this.y += (this.targetY - this.y) * this.speed;
    }

    draw() {
        // CSSのtransformを使用して描画（パフォーマンスが良い）
        this.el.style.transform = `translate(${this.x}px, ${this.y}px)`;
    }
}