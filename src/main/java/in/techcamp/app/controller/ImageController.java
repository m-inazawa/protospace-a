package in.techcamp.app.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.techcamp.app.entity.ImageEntity;
import in.techcamp.app.repository.ImageRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ImageController {
  private final ImageRepository imageRepository;
  @GetMapping("/prototype/{imageId}/image")
  public ResponseEntity<byte[]> getImageSrc(@PathVariable("imageId") Integer imageId) {
    ImageEntity imageEntity = imageRepository.findByImageId(imageId);

    if (imageEntity.getImage() == null || imageEntity.getImage().length <= 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
      .contentType(MediaType.IMAGE_PNG)
      .body(imageEntity.getImage());
  }
}
