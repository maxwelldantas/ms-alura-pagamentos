package com.github.maxwelldantas.pagamentos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDoPedido {

	private Long id;
	private Integer quantidade;
	private String descricao;
}
