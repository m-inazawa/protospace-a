// キャラクターのスペック定義

const DEFAULT_FRAME_COUNT = { n: 2, s: 2, e: 2, w: 2, ne: 2, nw: 2, se: 2, sw: 2 };

export const CharacterConfigs = {
  CAT: {
    id: 'cat',
    basePath: '/images/characters/cat/',
    frameCount: { ...DEFAULT_FRAME_COUNT },
    interval: 150,  // コマ送りの速度（ミリ秒）
    speed: 0.08,
    rushSpeed: 0.2
  },
  
  DOG: {
    id: 'dog',
    name: 'いぬ',
    basePath: '/images/characters/dog/',
    frameCount: 4,
    frames: { n: 4, s: 4, e: 4, w: 4, ne: 4, nw: 4, se: 4, sw: 4 },
    interval: 120,
    speed: 0.1, // 犬は猫より少し速い設定
    rushSpeed: 0.25
  }
};