document.getElementById('image').addEventListener('change', function(e) {
  const file = e.target.files[0];
  if (file) {
    document.getElementById('preview').src = URL.createObjectURL(file);
    document.getElementById('preview').style.display = 'block';
    document.querySelector('.form-group p').style.display = 'block';
  }
})