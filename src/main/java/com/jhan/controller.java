package com.jhan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by jhan on 3/27/16.
 */
@Controller
public class controller{
    @Autowired
    ProfileService profileService;

    @ResponseBody
    @RequestMapping("/")
    String entry() {
        return "SJSU 275 Lab 2";
    }

    @RequestMapping("/person")
    public String person(Model model) {
        model.addAttribute("person", profileService.findById("1"));
        //shopService = new ShopServiceImpl();
        //model.addAttribute("shop", shopService.findById(1));
        return "personview";
    }

    @RequestMapping(value = {"/profile/{id}", "/profile/{id}/"}, method = RequestMethod.GET)
    public String showProfile(@PathVariable String id, @RequestParam Map<String,String> allRequestParams, Model model){
        System.out.println("" + profileService.findById(id) );
        if(profileService.findById(id) == null ) throw new PageNotFoundException(id);
        model.addAttribute("profile", profileService.findById(id));
        if(allRequestParams.containsKey("brief")) {
            if(allRequestParams.get("brief").equals("true")){
                return "profileviewbrief";
            };
        }
        return "profileview";
    }

    @RequestMapping(value={"/profile/{id}","/profile/{id}/"}, method = RequestMethod.POST)
    public ModelAndView postProfile(@ModelAttribute Profile profile, @RequestParam(value="action") String action, @PathVariable Integer id, final RedirectAttributes redirectAttributes) {
       if(action.equals("delete")) {
           ModelAndView modelAndView = new ModelAndView("redirect:/redirectdelete");
           profile = profileService.delete(String.valueOf(id));
           return modelAndView;
       }
        ModelAndView modelAndView = new ModelAndView("redirect:/profile/" + id);
        if(profileService.findById(String.valueOf(id)) != null) {
            profileService.update(profile);
        }
        else {
            profileService.create(profile);
        }
        return modelAndView;
    }


    @RequestMapping(value={"/profile/{id}","/profile/{id}/"}, method = RequestMethod.DELETE)
    public ModelAndView deleteProfile(@PathVariable String id, final RedirectAttributes redirectAttributes) {
        if(profileService.findById(id) == null ) throw new PageNotFoundException(id);
        //ModelAndView modelAndView = new ModelAndView("redirect:/redirectdelete");
        ModelAndView modelAndView = new ModelAndView("profile");
        Profile profile = profileService.delete(id);
        //redirectAttributes.addFlashAttribute("message", "profile" + id + "was deleted!");
        Profile profileNew = new Profile();
        modelAndView.addObject("profile",profileNew);
        return modelAndView;
    }

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public ModelAndView showEmptyProfile() {
        Profile profile = new Profile();
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("profile",profile);
        return modelAndView;
    }

    @RequestMapping(value="/profile", method = RequestMethod.DELETE)
    public ModelAndView showProfileAfterDelete() {
        Profile profile = new Profile();
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("profile",profile);
        return modelAndView;
    }
    @RequestMapping(value="/profile", method = RequestMethod.POST)
    public ModelAndView createProfile(@ModelAttribute Profile profile, final RedirectAttributes redirectAttributes) {
        if(profile.getId() == null || profile.getId().equals("")) {
            ModelAndView modelAndView = new ModelAndView("redirect:/profile/");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/profile/" + profile.getId());
        if(profileService.findById(profile.getId()) != null) {
            profileService.update(profile);
        }
        else {
            profileService.create(profile);
        }
        return modelAndView;
    }


    @RequestMapping(value="/redirectdelete")
    public View redirectDelete(final RedirectAttributes redirectAttributes) {
        RedirectView redirect = new RedirectView("profile");
        redirect.setExposeModelAttributes(false);
        return redirect;
    }

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() throws Exception{
        return "404";
    }


}
