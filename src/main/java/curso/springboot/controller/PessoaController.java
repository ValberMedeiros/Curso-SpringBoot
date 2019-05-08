package curso.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
	TelefoneRepository telefoneRepository;
	
	@RequestMapping(method = RequestMethod.GET, value="/cadastropessoa")
	public ModelAndView inicio() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="**/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		andView.addObject("pessoaobj", pessoa.get());
		return andView;
	}
	
	
	
	@GetMapping("/deletarpessoa/{idpessoa}")
	public ModelAndView deletar(@PathVariable("idpessoa") Long idpessoa) {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		pessoaRepository.deleteById(idpessoa);
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@PostMapping("**/pesquisapessoa")
	public ModelAndView pesquisar (@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	@GetMapping("/detalharpessoa/{idpessoa}")
	public ModelAndView detalhar(@PathVariable("idpessoa") Long idpessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView andView = new ModelAndView("cadastro/detalhepessoa");
		andView.addObject("pessoaobj", pessoa.get());
		andView.addObject("telefones", telefoneRepository.findAllTelefoneByPessoaId(idpessoa));
		andView.addObject("telefone", new Telefone());
		return andView;
	}
	
	@PostMapping("**/salvartelefonePessoa/{pessoaid}")
	public ModelAndView salvartelefonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {
		ModelAndView modelAndView = new ModelAndView("cadastro/detalhepessoa");
		Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaid);
		
		telefone.setPessoa(pessoa.get());
		telefoneRepository.save(telefone);
		modelAndView.addObject("telefones", telefoneRepository.findAllTelefoneByPessoaId(pessoaid));
		
		modelAndView.addObject("pessoaobj", pessoa.get());
		return modelAndView;
	}
	
	@GetMapping("**/editarTelefonePessoa/{telefoneid}")
	public ModelAndView editarTelefonePessoa(@PathVariable("telefoneid") Long telefoneid) {
		ModelAndView modelAndView = new ModelAndView("cadastro/detalhepessoa");
		Optional<Telefone> telefone = telefoneRepository.findById(telefoneid);
		
		Pessoa pessoa = telefoneRepository.findById(telefoneid).get().getPessoa();
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.findAllTelefoneByPessoaId(pessoa.getId()));
		modelAndView.addObject("telefone", telefone.get());
		
		return modelAndView;
	}
	
	@GetMapping("**/deletarTelefonePessoa/{telefoneid}")
	public ModelAndView deletarTelefonePessoa(@PathVariable("telefoneid") Long telefoneid) {
		ModelAndView modelAndView = new ModelAndView("cadastro/detalhepessoa");
		
		Pessoa pessoa = telefoneRepository.findById(telefoneid).get().getPessoa();
		
		telefoneRepository.deleteById(telefoneid);
		
		
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.findAllTelefoneByPessoaId(pessoa.getId()));
		modelAndView.addObject("telefone", new Telefone());
		
		return modelAndView;
	}
}
