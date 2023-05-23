package propensi.project.water.repository.PenawaranOlahan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import propensi.project.water.model.PenjualanHasilOlahan.ItemPenawaranOlahanModel;
import propensi.project.water.model.PenjualanHasilOlahan.PenawaranOlahanModel;

import java.util.List;

@Repository
public interface ItemPenawaranOlahanDb  extends JpaRepository<ItemPenawaranOlahanModel, String> {
    void deleteAllByIdPenawaranOlahan(PenawaranOlahanModel penawaranOlahan);
}