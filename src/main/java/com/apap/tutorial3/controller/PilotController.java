package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "flyHour", required = true) int flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}
	
	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}
	
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model){
		List<PilotModel> archive = pilotService.getPilotList();
		
		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}
	
	@RequestMapping(value = {"/pilot/view/license-number", "/pilot/view/license-number/{licenseNumber}"})
	public String viewPath(@PathVariable Optional<String> licenseNumber, Model model) {
		if(licenseNumber.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if(archive != null) {
				model.addAttribute("pilot", archive);
				return "view-pilot";
			}
			else {
				model.addAttribute("typeError", "licenseNumber tidak ditemukan");
				return "error";
			}
		}
		else {
			model.addAttribute("typeError", "licenseNumber kosong");
			return "error";
		}
	}
	
	@RequestMapping(value = {"/pilot/update/license-number", "/pilot/update/license-number/{licenseNumber}", "/pilot/update/license-number/{licenseNumber}/fly-hour", "/pilot/update/license-number/{licenseNumber}/fly-hour/{flyHour}"})
	public String updatePath(@PathVariable Optional<String> licenseNumber, @PathVariable Optional<String> flyHour, Model model) {
		if(licenseNumber.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if(archive != null) {
				if(flyHour.isPresent()) {
					int hour = Integer.parseInt(flyHour.get());
					archive.setFlyHour(hour);
					return "update";
				}
				else {
					model.addAttribute("typeError", "flyHour kosong");
					return "error";
				}
			}
			else {
				model.addAttribute("typeError", "licenseNumber tidak ditemukan");
				return "error";
			}
		}
		else {
			model.addAttribute("typeError", "licenseNumber kosong");
			return "error";
		}
	}
	
	@RequestMapping(value = {"/pilot/delete/id", "/pilot/delete/id/{id}"})
	public String delete(@PathVariable Optional<String> id, Model model) {
		if(id.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailById(id.get());
			if(archive != null) {
				pilotService.deletePilot(archive);
				return "delete";
			}
			else {
				model.addAttribute("typeError", "id tidak ditemukan");
				return "error";
			}
		}
		else {
			model.addAttribute("typeError", "id kosong");
			return "error";
		}
	}
}
