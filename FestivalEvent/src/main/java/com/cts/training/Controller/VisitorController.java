package com.cts.training.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.cts.training.Entity.EventEntity;
import com.cts.training.Entity.VisitorEntity;
import com.cts.training.Service.EventService;
import com.cts.training.Service.VisitorServiceInterface;
import com.cts.training.bean.VisitorLoginBean;
import com.cts.training.bean.VisitorRegBean;

@Controller
@RequestMapping(value = "/festival")
public class VisitorController {

	@Autowired
	private VisitorServiceInterface service;

	@Autowired
	private EventService eService;

	
	
	@RequestMapping(value = "/registration.html")
	public ModelAndView registerVisitor() {
		System.out.println("register visitor");
		return new ModelAndView("registration", "registrationAttribute", new VisitorRegBean());
	}

	@RequestMapping(value = "/save.html", method = RequestMethod.POST)
	public ModelAndView getValues(@ModelAttribute("registrationAttribute") VisitorRegBean visitorBean, Model theModel) {
		System.out.println("save details");
		theModel.addAttribute("vLogin", new VisitorLoginBean());
		VisitorEntity vEntity = service.getVisitorObject(visitorBean.getUserName());
		if (vEntity != null) {
			theModel.addAttribute("isRegistered", "username already exists, login");
			return new ModelAndView("index");
		} else {
			service.saveVisitor(visitorBean);

			return new ModelAndView("login");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String setupForm(Model model) {
		VisitorLoginBean vLogin = new VisitorLoginBean();
		
		model.addAttribute("vLogin", vLogin);
		return "login";
	}

	@RequestMapping(value = "/searchVisitor.html", method = RequestMethod.POST)
	public String submitForm(@ModelAttribute("vLogin") VisitorLoginBean vLogin, BindingResult result,
			SessionStatus status, Model theModel) {
		
		boolean error = false;

		System.out.println(vLogin); 
		if (vLogin.getUserName().isEmpty() == true || vLogin.getPassWord().isEmpty() == true) {
			if (vLogin.getUserName().isEmpty()) {
				result.rejectValue("userName", "error.userName");
				error = true;
			}
			if (vLogin.getPassWord().isEmpty()) {
				result.rejectValue("passWord", "error.passWord");
				error = true;
			}
		}
		if ((vLogin.getUserName().isEmpty()) == false && (vLogin.getPassWord().isEmpty()) == false) {
			String out = service.validateVisitorLogin(vLogin.getUserName(), vLogin.getPassWord());

			if (out.equals("notFound")) {
				result.rejectValue("userName", "error.noUserFound");
				error = true;
			}
			if (out.equals("incorrect")) {
				result.rejectValue("passWord", "error.wrongPassWord");
				error = true;
			}
		}
		if (error) {
			return "login";
		}

		theModel.addAttribute("visitor", service.getVisitorObject(vLogin.getUserName()));
		theModel.addAttribute("allEvents", eService.showEvent());
		List<EventEntity> list = eService.getRegisteredEvent(service.getId(vLogin.getUserName()));
		if (list.isEmpty()) {
			theModel.addAttribute("messageEvent", "Currently you have not registered to any event.");
		} else {
			theModel.addAttribute("regEvents", list);
		}
		status.setComplete();
		return "Mainform";
	}

	
	@RequestMapping(value = "/update.html")
	public String updateVisistorDetail(@RequestParam("vName") String userName, Model theModel) {
		System.out.println("Inside UpdateVisitor Detail");
		VisitorEntity vEntity = service.getVisitorObject(userName);
		theModel.addAttribute("registrationAttribute", vEntity);

		return "update";
	}

	@RequestMapping(value = "/updateVisitor.html", method = RequestMethod.POST)
	public String updateValues(@ModelAttribute("registrationAttribute") VisitorRegBean visitorBean, Model theModel) {
		theModel.addAttribute("visitor", service.updateVisitorObject(visitorBean));

		System.out.println(visitorBean);
		theModel.addAttribute("vLogin", new VisitorLoginBean());
		theModel.addAttribute("allEvents", eService.showEvent());
		List<EventEntity> list = eService.getRegisteredEvent(service.getId(visitorBean.getUserName()));
		if (list.isEmpty()) {
			theModel.addAttribute("messageEvent", "Currently you have not registered to any event.");
		} else {
			theModel.addAttribute("regEvents", list);
		}
		theModel.addAttribute("visitor", service.getVisitorObject(visitorBean.getUserName()));
		theModel.addAttribute("message", "Details updated Successfully");
		return "Mainform";
	}

	@RequestMapping(value = "/changepassword.html")
	public ModelAndView changePWD(@RequestParam("uName") String userName, Model theModel) {
		theModel.addAttribute("status", userName);
		return new ModelAndView("ChangePassword");
	}

	@RequestMapping(value = "/updatepassword.html", method = RequestMethod.POST)
	public ModelAndView updatePwd(@RequestParam("status") String userName, @RequestParam("password") String pwd,
			Model theModel) {
		boolean out = service.changePassword(userName, pwd);
		if (out == true) {
			theModel.addAttribute("message", "Password Changed SuccessFully");
		} else {
			theModel.addAttribute("message", "something Happened, unable to change password");

		}
		List<EventEntity> list = eService.getRegisteredEvent(service.getId(userName));
		if (list.isEmpty()) {
			theModel.addAttribute("messageEvent", "Currently you have not registered to any event.");
		} else {
			theModel.addAttribute("regEvents", list);
		}
		theModel.addAttribute("visitor", service.getVisitorObject(userName));
		return new ModelAndView("Mainform");

	}

	@RequestMapping(value = "/eventregistration.html")
	public ModelAndView toRegisterEent(@RequestParam("visitorId") int visitorId, @RequestParam("eventId") int eventId,
			Model theModel) {
		if (!eService.isAlreadyRegistered(visitorId, eventId)) {
			theModel.addAttribute("event", eService.showEvent(eventId));
			theModel.addAttribute("visitorId", visitorId);
			return new ModelAndView("EventRegistration");
		} else
			theModel.addAttribute("visitor", service.getVisitor(visitorId));
		List<EventEntity> list = eService.getRegisteredEvent(visitorId);
		if (list.isEmpty()) {
			theModel.addAttribute("messageEvent", "Currently you have not registered to any event.");
		} else {
			theModel.addAttribute("regEvents", list);
		}
		theModel.addAttribute("message", "You have already booked tickets for this event");
		theModel.addAttribute("allEvents", eService.showEvent());
		return new ModelAndView("Mainform");
	}

	@RequestMapping(value = "/confirmSeats.html", method = RequestMethod.POST)
	public String confirmBooking(@RequestParam("visitorId") String visitorId, @RequestParam("eventId") String eventId,
			@RequestParam("noOfSeats") String seats, Model theModel) {

		boolean out = eService.registeredToevent(Integer.parseInt(eventId), Integer.parseInt(visitorId),
				Integer.parseInt(seats));
		List<EventEntity> list = eService.getRegisteredEvent(Integer.parseInt(visitorId));

		if (out == true) {
			if (list.isEmpty()) {
				theModel.addAttribute("messageEvent", "Currently you have not registered to any event.");
			} else {
				theModel.addAttribute("regEvents", list);

			}
			theModel.addAttribute("regEvents", list);
			theModel.addAttribute("visitor", service.getVisitor(Integer.parseInt(visitorId)));
			theModel.addAttribute("message", "Tickets Booked for the Event");
			theModel.addAttribute("allEvents", eService.showEvent());
			return "Mainform";
		}
		theModel.addAttribute("visitor", service.getVisitor(Integer.parseInt(visitorId)));
		theModel.addAttribute("allEvents", eService.showEvent());
		theModel.addAttribute("message", "Unable to book tickets, try again after sometime");
		return "Mainform";

	}

	@RequestMapping("/eventunregistration.html")
	public String cancelTicket(@RequestParam("") String visitorId, @RequestParam("") String eventId, Model theModel) {
		boolean out = eService.cancelEventTicket(Integer.parseInt(visitorId), Integer.parseInt(eventId));

		List<EventEntity> list = eService.getRegisteredEvent(Integer.parseInt(visitorId));

		if (list.isEmpty()) {
			theModel.addAttribute("messageEvent", "Currently you have not registered to any event.");
		} else {
			theModel.addAttribute("regEvents", list);

		}
		// theModel.addAttribute("regEvents", list);
		if (out == true) {
			theModel.addAttribute("message", "Tickets canceled succesfully");
		} else {
			theModel.addAttribute("message", "Unable to cancel the tickets, try again after sometime");
		}
		theModel.addAttribute("visitor", service.getVisitor(Integer.parseInt(visitorId)));
		theModel.addAttribute("allEvents", eService.showEvent());
		return "Mainform";

	}
	@RequestMapping("/about.html")
	public String aboutPage()
	{
	return "About";	
	}
	@RequestMapping("/catalog.html")
	public String eventcatalog(Model theModel)
	{
		theModel.addAttribute("allEvents", eService.showEvent());

		return "EventCatalog";
	}
	@RequestMapping("/logout.html")
	public String logout(Model theModel)
	{
		
		return "index";
	}

}
