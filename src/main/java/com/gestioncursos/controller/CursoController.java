package com.gestioncursos.controller;

import com.gestioncursos.model.Curso;
import com.gestioncursos.reports.ExporterExcel;
import com.gestioncursos.reports.ExporterPDF;
import com.gestioncursos.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
//@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public String home() {
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listaCursos(Model model) {
        List<Curso> cursos;
        cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
        return "cursos";
    }

    @GetMapping("/cursos/nuevo")
    public String agregarCursos(Model model) {
        Curso curso = new Curso();
        curso.setPublicado(true);
        model.addAttribute("curso", curso);
        model.addAttribute("pageTitle", "Nuevo curso");
        return "cursoForm";
    }

    @PostMapping("/cursos/guardar")
    public String guardarCursos(Curso curso, RedirectAttributes redirectAttributes) {
        try {
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("message", "El curso ha sido guardado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("/cursos/{id}")
    public String editarCursos(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Curso curso = cursoRepository.findById(id).orElse(null);
            model.addAttribute("curso", curso);
            model.addAttribute("pageTitle", "Editar curso: " + id);
            return "cursoForm";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @PostMapping("/cursos/eliminar/{id}")
    public String eliminarCursos(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            cursoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "El curso ha sido eliminado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("/export/pdf")
    public void generarReportePdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cursos" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

        List<Curso> cursos = cursoRepository.findAll();

        ExporterPDF exporterPDF = new ExporterPDF(cursos);
        exporterPDF.export(response);
    }

    @GetMapping("/export/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cursos" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

        List<Curso> cursos = cursoRepository.findAll();

        ExporterExcel exporterExcel = new ExporterExcel(cursos);
        exporterExcel.export(response);
    }
}
