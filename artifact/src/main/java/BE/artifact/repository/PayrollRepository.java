package BE.artifact.repository;

import BE.artifact.model.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long>{
    Page<Payroll> findByUserEmail(String email, Pageable pageable);
    void deleteByUserEmail(String email);
}
