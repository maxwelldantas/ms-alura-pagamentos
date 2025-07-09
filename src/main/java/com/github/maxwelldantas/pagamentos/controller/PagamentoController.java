package com.github.maxwelldantas.pagamentos.controller;

import com.github.maxwelldantas.pagamentos.dto.PagamentoDTO;
import com.github.maxwelldantas.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

	private final PagamentoService pagamentoService;

	@PostMapping
	public ResponseEntity<PagamentoDTO> cadastrar(@RequestBody @Valid PagamentoDTO dto, UriComponentsBuilder uriBuilder) {
		var pagamento = pagamentoService.criarPagamento(dto);
		URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

		return ResponseEntity.created(endereco).body(pagamento);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PagamentoDTO> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDTO dto) {
		var atualizado = pagamentoService.atualizarPagamento(id, dto);

		return ResponseEntity.ok(atualizado);
	}

	@GetMapping
	public Page<PagamentoDTO> listar(@PageableDefault Pageable pageable) {
		return pagamentoService.obterTodos(pageable);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PagamentoDTO> detalhar(@PathVariable @NotNull Long id) {
		var dto = pagamentoService.obterPorId(id);

		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<PagamentoDTO> remover(@PathVariable @NotNull Long id) {
		pagamentoService.excluirPagamento(id);

		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/confirmar")
	public ResponseEntity<PagamentoDTO> confirmarPagamento(@PathVariable @NotNull Long id) {
		pagamentoService.confirmarPagamento(id);

		return ResponseEntity.noContent().build();
	}
}
