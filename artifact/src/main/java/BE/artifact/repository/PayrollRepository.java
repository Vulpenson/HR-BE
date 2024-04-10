package BE.artifact.repository;

import BE.artifact.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long>{
    List<Payroll> findByUserEmail(String email);
    void deleteByUserEmail(String email);
}
