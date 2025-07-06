package com.github.maxwelldantas.pagamentos.repository;

import com.github.maxwelldantas.pagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
