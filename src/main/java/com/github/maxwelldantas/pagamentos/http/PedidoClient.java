package com.github.maxwelldantas.pagamentos.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "pedidos-ms")
public interface PedidoClient {

	@PutMapping(value = "/pedidos/{id}/pago")
	void atualizarPagamento(@PathVariable Long id);
}
