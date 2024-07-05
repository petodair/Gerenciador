package br.com.gerenciadados.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.gerenciadados.dao.ProdutoDao;
import br.com.gerenciadados.model.Produto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class ProdutoController {

	@Autowired
	private ProdutoDao produtoRepositorio;

	@GetMapping("/inserirProduto")
	public ModelAndView insertProduto(Produto produto) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/formProduto");
		mv.addObject("produto", new Produto());
		return mv;
	}

	@PostMapping("/insertProduto")
	public ModelAndView addProduto(@Valid Produto produto, BindingResult br) {
		ModelAndView mv = new ModelAndView();
		if (br.hasErrors()) {
			mv.setViewName("produto/formProduto");
			mv.addObject("produto");
			List<String> errors = br.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
			System.out.println(produto.getValidade());
			System.out.println(errors);
			
		} else {
			mv.setViewName("redirect:/listaDeProdutos");
			produtoRepositorio.save(produto);
		}
		return mv;

	}

	@GetMapping("/listaDeProdutos")
	public ModelAndView insertProduto() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/listProduto");
		mv.addObject("produtosList", produtoRepositorio.findAll());
		return mv;
	}

	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable("id") Integer id) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/alterar");

		// pegar pelo id
		Produto produto = produtoRepositorio.getReferenceById(id);
		mv.addObject("produto", produto);
		return mv;
	}

	@PostMapping("/alterar")
	public ModelAndView alterar(Produto produto) {
		ModelAndView mv = new ModelAndView();
		produtoRepositorio.save(produto);
		mv.setViewName("redirect:/listaDeProdutos");
		return mv;
	}

	@GetMapping("/excluir/{id}")
	public String excluirProduto(@PathVariable("id") Integer id) {
		produtoRepositorio.deleteById(id);
		return "redirect:/listaDeProdutos";
	}

	@GetMapping("/filtroProdutos")
	public ModelAndView filtroProdutos(Produto produto) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/filtroProdutos");
		return mv;
	}

	@GetMapping("/produtosVacuo")
	public ModelAndView filtroProdutosVacuo(Produto produto) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/produtosVacuo");
		mv.addObject("produtosVacuo", produtoRepositorio.findByTipoVacuo());
		return mv;
	}

	@GetMapping("/produtosBovinos")
	public ModelAndView filtroProdutosBovinos(Produto produto) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/produtosBovinos");
		mv.addObject("produtosBovinos", produtoRepositorio.findByTipoBovino());
		return mv;
	}

	@GetMapping("/produtosFrangos")
	public ModelAndView filtroProdutosFrangos(Produto produto) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/produtosFrangos");
		mv.addObject("produtosFrangos", produtoRepositorio.findByTipoFrango());
		return mv;
	}

	@GetMapping("/produtosSuinos")
	public ModelAndView filtroProdutosSuinos(Produto produto) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/produtosSuinos");
		mv.addObject("produtosSuinos", produtoRepositorio.findByTipoSuino());
		return mv;
	}

	@GetMapping("/produtosCongelados")
	public ModelAndView filtroProdutosCongelados(Produto produto) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("produto/produtosCongelados");
		mv.addObject("produtosCongelados", produtoRepositorio.findByTipoCongelado());
		return mv;
	}

	@PostMapping("/pesquisarProduto")
	public ModelAndView pesquisarProduto(@RequestParam(required = false) String nome) {
		ModelAndView mv = new ModelAndView();
		List<Produto> listaProdutos;
		if (nome == null || nome.trim().isEmpty()) {
			listaProdutos = produtoRepositorio.findAll();
		} else {
			listaProdutos = produtoRepositorio.findByNomeContainingIgnoreCase(nome);
		}
		mv.addObject("listaDeProdutos", listaProdutos);
		mv.addObject("pesquisa", nome);
		mv.setViewName("produto/pesquisaResultado");
		return mv;
	}
	
	@GetMapping("/gerarRelatorio/{tipo}")
	public ModelAndView gerarRelatorio(@PathVariable("tipo") String tipo, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Document documento = new Document();
		try {
			
			List<Produto> lista = selecionarLista(tipo);
			
			response.setContentType("application/pdf");
			
			response.addHeader("Content-Disposition", "inline; filename=produtos.pdf");
			
			PdfWriter.getInstance(documento, response.getOutputStream());
			
			documento.open();
			
			documento.add(new Paragraph("Lista de Produtos:"));
			
			documento.add(new Paragraph(" "));
			
			PdfPTable tabela = new PdfPTable(6);
			
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Código"));
			PdfPCell col3 = new PdfPCell(new Paragraph("Fabricação"));
			PdfPCell col4 = new PdfPCell(new Paragraph("Validade"));
			PdfPCell col5 = new PdfPCell(new Paragraph("Quantidade"));
			PdfPCell col6 = new PdfPCell(new Paragraph("Tipo"));
			
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			tabela.addCell(col4);
			tabela.addCell(col5);
			tabela.addCell(col6);
			
			
			for (int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getCodigo());
				tabela.addCell(converterDataParaString(lista.get(i).getFabricacao()));
				tabela.addCell(converterDataParaString(lista.get(i).getValidade()));
				tabela.addCell(Double.toString(lista.get(i).getQuantidade()));
				tabela.addCell(lista.get(i).getTipo().toString());
			}
			documento.add(tabela);
			documento.close();
		} catch (Exception e) {
			System.out.println(e);
			documento.close();
		}
		return mv;
	}
	
	@GetMapping("/gerarRelatorioPesquisa/{pesquisa}")
	public ModelAndView gerarRelatorioPesquisa(@PathVariable("pesquisa") String pesquisa, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Document documento = new Document();
		try {
			
			List<Produto> lista;
			
			if(pesquisa == null || pesquisa.trim().isEmpty()) {
			    lista = produtoRepositorio.findAll();
			} else {
				lista = produtoRepositorio.findByNomeContainingIgnoreCase(pesquisa);
			}
			response.setContentType("application/pdf");
			
			response.addHeader("Content-Disposition", "inline; filename=produtos.pdf");
			
			PdfWriter.getInstance(documento, response.getOutputStream());
			
			documento.open();
			
			documento.add(new Paragraph("Lista de Produtos:"));
			
			documento.add(new Paragraph(" "));
			
			PdfPTable tabela = new PdfPTable(6);
			
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Código"));
			PdfPCell col3 = new PdfPCell(new Paragraph("Fabrição"));
			PdfPCell col4 = new PdfPCell(new Paragraph("Validade"));
			PdfPCell col5 = new PdfPCell(new Paragraph("Quantidade"));
			PdfPCell col6 = new PdfPCell(new Paragraph("Tipo"));
			
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			tabela.addCell(col4);
			tabela.addCell(col5);
			tabela.addCell(col6);
			
			
			for (int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getCodigo());
				tabela.addCell(converterDataParaString(lista.get(i).getFabricacao()));
				tabela.addCell(converterDataParaString(lista.get(i).getValidade()));
				tabela.addCell(Double.toString(lista.get(i).getQuantidade()));
				tabela.addCell(lista.get(i).getTipo().toString());
			}
			documento.add(tabela);
			documento.close();
		} catch (Exception e) {
			System.out.println(e);
			documento.close();
		}
		return mv;
	}

	public String converterDataParaString(Date data) {

		DateFormat formatBR = new SimpleDateFormat("dd-MM-yyyy");
		String dataConvertida = formatBR.format(data);

		return dataConvertida;
	}

	
	
	public List<Produto> selecionarLista(String tipo) {
		if(tipo.equals("VACUO")) {
			return produtoRepositorio.findByTipoVacuo();
		} else if(tipo.equals("BOVINO")) {
			return produtoRepositorio.findByTipoBovino();
		} else if(tipo.equals("SUINO")) {
			return produtoRepositorio.findByTipoSuino();
		} else if(tipo.equals("FRANGO")) {
			return produtoRepositorio.findByTipoFrango();
		} else if (tipo.equals("CONGELADO")) {
			return produtoRepositorio.findByTipoCongelado();
		} else {
			return null;
		}
	}

}
