// キャラクターのスペック定義

const DEFAULT_FRAME_COUNT = { n: 2, s: 2, e: 2, w: 2, ne: 2, nw: 2, se: 2, sw: 2 };

export const CharacterConfigs = {
  CAT: {
    id: 'cat',
    basePath: '/images/characters/cat/',
    frameCount: { ...DEFAULT_FRAME_COUNT },
    interval: 150  // コマ送りの速度（ミリ秒）
  }
};