function toggleFollow(button) {
  const userId = button.getAttribute('data-id');
  const currentFlg = button.getAttribute('data-flg');
  // Fetch APIで非同期リクエスト
  fetch(`/users/follow/${userId}/${currentFlg}`, {
      method: 'POST'
  })
  .then(response => {
    if (response.ok) {
      // 通信成功時にボタンの表示を書き換える
      const iconElm = document.getElementById('follow-icon');
      // フォロー解除
      if (currentFlg === 'follow') {
        // フォロー解除処理（見た目を未フォローへ）
        button.innerText = 'フォロー';
        button.setAttribute('data-flg', 'unFollow');
        button.classList.replace('follow-btn', 'unFollow-btn');

        if (iconElm) {
          iconElm.classList.replace('icon-following', 'icon-not-following');
          iconElm.innerText = '';
        }
      } else {
          // フォロー実行処理（見た目をフォロー中へ）
          button.innerText = 'フォロー解除';
          button.setAttribute('data-flg', 'follow');
          button.classList.replace('unFollow-btn', 'follow-btn');
      
          if (iconElm) {
            iconElm.classList.replace('icon-not-following', 'icon-following');
            iconElm.innerText = 'フォロー中';
          }
      }
    }
  })
  .catch(error => console.error('Error:', error));
}

