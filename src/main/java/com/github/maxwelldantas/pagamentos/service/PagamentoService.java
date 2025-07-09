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
				.map(pagamento -> modelMapper.map(pagamento, PagamentoDTO.class));
	}

	public PagamentoDTO obterPorId(Long id) {
		var pagamento = pagamentoRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		return modelMapper.map(pagamento, PagamentoDTO.class);
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
		var pagamento = pagamentoRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		pagamento.setStatus(Status.CONFIRMADO);
		pagamentoRepository.save(pagamento);
		pedidoClient.atualizarPagamento(pagamento.getPedidoId());
	}
}
