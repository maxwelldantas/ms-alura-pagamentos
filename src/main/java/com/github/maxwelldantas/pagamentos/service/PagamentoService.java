package com.github.maxwelldantas.pagamentos.service;

import com.github.maxwelldantas.pagamentos.dto.PagamentoDTO;
import com.github.maxwelldantas.pagamentos.http.PedidoClient;
import com.github.maxwelldantas.pagamentos.model.Pagamento;
import com.github.maxwelldantas.pagamentos.model.Status;
import com.github.maxwelldantas.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

	private final PagamentoRepository pagamentoRepository;
	private final ModelMapper modelMapper;
	private final PedidoClient pedidoClient;

	public Page<PagamentoDTO> obterTodos(Pageable pageable) {
		return pagamentoRepository
				.findAll(pageable)
				.map(pagamento -> {
					var dto = modelMapper.map(pagamento, PagamentoDTO.class);
					dto.setItens(pedidoClient.obterItensDoPedido(pagamento.getPedidoId()).getItens());
					return dto;
				});
	}

	public PagamentoDTO obterPorId(Long id) {
		var pagamento = pagamentoRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		var dto = modelMapper.map(pagamento, PagamentoDTO.class);
		dto.setItens(pedidoClient.obterItensDoPedido(pagamento.getPedidoId()).getItens());

		return dto;
	}

	@Transactional
	public PagamentoDTO criarPagamento(PagamentoDTO pagamentoDTO) {
		var pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
		pagamento.setStatus(Status.CRIADO);

		pagamentoRepository.save(pagamento);

		return modelMapper.map(pagamento, PagamentoDTO.class);
	}

	@Transactional
	public PagamentoDTO atualizarPagamento(Long id, PagamentoDTO pagamentoDTO) {
		pagamentoRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		var pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
		pagamento.setId(id);

		pagamento = pagamentoRepository.save(pagamento);

		return modelMapper.map(pagamento, PagamentoDTO.class);
	}

	@Transactional
	public void excluirPagamento(Long id) {
		pagamentoRepository.deleteById(id);
	}

	@Transactional
	public void confirmarPagamento(Long id) {
		confirmarPagamentoStatus(id, false);
	}

	@Transactional
	public void alteraStatus(Long id) {
		confirmarPagamentoStatus(id, true);
	}

	private void confirmarPagamentoStatus(Long id, boolean isFallback) {
		var pagamento = pagamentoRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		if (!isFallback) {
			pagamento.setStatus(Status.CONFIRMADO);
		}
		if (isFallback) {
			pagamento.setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
		}

		pagamentoRepository.save(pagamento);

		if (!isFallback) {
			pedidoClient.atualizarPagamento(pagamento.getPedidoId());
		}
	}
}
