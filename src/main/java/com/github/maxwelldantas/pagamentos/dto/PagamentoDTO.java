package com.github.maxwelldantas.pagamentos.dto;

import com.github.maxwelldantas.pagamentos.model.ItemDoPedido;
import com.github.maxwelldantas.pagamentos.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PagamentoDTO {

	private Long id;
	private BigDecimal valor;
	private String nome;
	private String numero;
	private String expiracao;
	private String codigo;
	private Status status;
	private Long pedidoId;
	private Long formaDePagamentoId;
	private List<ItemDoPedido> itens;
}
