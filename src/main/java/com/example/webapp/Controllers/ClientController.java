package com.example.webapp.Controllers;

import com.example.webapp.models.Client;
import com.example.webapp.models.ClientDto;
import com.example.webapp.repositories.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepo;
    @GetMapping({"", "/"})
    public String getClients(Model model) {
        List<Client> clients = clientRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("data", clients);
        return "clients/index";
    }
    @GetMapping("/create")
    public String createClient(Model model) {
        ClientDto clientDto = new ClientDto();
        model.addAttribute("clientDto", clientDto);
        return "clients/create";
    }
    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute ClientDto clientDto, BindingResult result) {

        if(clientRepo.findByEmail(clientDto.getEmail()) != null) {
            result.addError(new FieldError("clientDto", "email", clientDto.getEmail(), false, null, null, "Email address is already used"));
        }


        if(result.hasErrors()) {
            return "clients/create";
        }


        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());
        client.setCreatedAt(new Date(System.currentTimeMillis()));

        clientRepo.save(client);

        return "redirect:/clients";
    }
    @GetMapping("/edit")
    public String editClient(Model model,@RequestParam int id){
        Client client= clientRepo.findById(id).orElse(null);
        if(client==null){
            return  "redirect:/clients";
        }
        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName(client.getFirstName());
        clientDto.setLastName(client.getLastName());
        clientDto.setEmail(client.getEmail());
        clientDto.setPhone(client.getPhone());
        clientDto.setAddress(client.getAddress());
        clientDto.setStatus(client.getStatus());
        model.addAttribute("clientDto",clientDto);
        model.addAttribute("client",client);
        return  "clients/edit";
    }
    @PostMapping("/edit")
    public String editClient(
            @RequestParam int id, // Use @PathVariable to capture the ID
            @Valid @ModelAttribute ClientDto clientDto,
            BindingResult result,
            Model model
    ) {
        Client client = clientRepo.findById(id).orElse(null);
        if (client == null) {
            return "redirect:/clients";
        }

        model.addAttribute("client", client);
        if (result.hasErrors()) {
            return "clients/edit";
        }

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());

        try {
            clientRepo.save(client);
        } catch (Exception ex) {
            result.addError(
                    new FieldError(
                            "clientDto", "email", clientDto.getEmail(),
                            false, null, null, "Email address is already used"
                    )
            );
            return "clients/edit";
        }

        return "redirect:/clients";
    }
@GetMapping("/delete")
    public  String deleteClient(@RequestParam int id)
{
    Client client =clientRepo.findById(id).orElse(null);
    if(client !=null)
    {
        clientRepo.delete(client);
    }
    return  "redirect:/clients";
}
}

