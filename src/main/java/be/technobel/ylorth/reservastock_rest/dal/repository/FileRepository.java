package be.technobel.ylorth.reservastock_rest.dal.repository;

import be.technobel.ylorth.reservastock_rest.dal.models.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
