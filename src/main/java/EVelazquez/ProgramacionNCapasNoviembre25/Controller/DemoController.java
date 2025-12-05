/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EVelazquez.ProgramacionNCapasNoviembre25.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoController {
 
@GetMapping("/Hola/{Nombre}")
public String Hola(@PathVariable String Nombre, Model model){

    model.addAttribute("NombrePersona", Nombre);
    
    return "Hola";
}

@GetMapping ("/Suma")
public String Suma(@RequestParam int NumeroUno, @RequestParam int NumeroDos, Model model){

    int resultado = NumeroUno + NumeroDos;
    
    model.addAttribute("resultado", resultado);
    
    return "Hola";
}
   
}
