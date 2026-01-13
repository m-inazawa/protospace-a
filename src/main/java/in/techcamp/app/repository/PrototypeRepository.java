package in.techcamp.app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.techcamp.app.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {

  @Select("SELECT * FROM prototypes")
  List<PrototypeEntity> findAll();
}
