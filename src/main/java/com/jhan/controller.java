package com.jhan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView postProfile(@ModelAttribute Profile profile, @PathVariable Integer id, final RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView("redirect:/profile/" + id);
       // ModelAndView mav = new ModelAndView();
        //String message = "Shop was successfully updated.";
        /*
        if(profile != null) {
            System.out.println(profile.getFirstname());
            if(profile.getFirstname() != null)
                profileService.update(profile);
        }
        else {
            Profile profileNew = new Profile();
            profileNew.setId(String.valueOf(id));
            if(allRequestParams.containsKey("firstname")) {
                profileNew.setFirstname(allRequestParams.get("firstaname"));
            }
            if(allRequestParams.containsKey("lastname")) {
                profileNew.setLastname(allRequestParams.get("lastname"));
            }
            if(allRequestParams.containsKey("email")) {
                profileNew.setEmail(allRequestParams.get("email"));
            }
            if(allRequestParams.containsKey("address")) {
                profileNew.setAddress(allRequestParams.get("address"));
            }
            if(allRequestParams.containsKey("organization")) {
                profileNew.setOrganization(allRequestParams.get("organization"));
            }
            if(allRequestParams.containsKey("aboutMyself")) {
                profileNew.setAboutMyself(allRequestParams.get("aboutMyself"));
            }
            if(profileService.findById(String.valueOf(id)) != null) {
                profileService.update(profileNew);
            }
            else {
                profileService.create(profileNew);
            }
        }
        */
        if(profileService.findById(String.valueOf(id)) != null) {
            profileService.update(profile);
        }
        else {
            profileService.create(profile);
        }

        return mav;
    }

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public ModelAndView showEmptyProfile() {
        Profile profile = new Profile();
        ModelAndView mav = new ModelAndView("/profile");
        mav.addObject("profile",profile);
        return mav;
    }
    @RequestMapping(value="/profile", method = RequestMethod.POST)
    public ModelAndView createProfile(@ModelAttribute Profile profile, final RedirectAttributes redirectAttributes) {
        if(profile.getId() == null || profile.getId().equals("")) {
            ModelAndView mav = new ModelAndView("redirect:/profile/");
            return mav;
        }
        ModelAndView mav = new ModelAndView("redirect:/profile/" + profile.getId());
        if(profileService.findById(profile.getId()) != null) {
            profileService.update(profile);
        }
        else {
            profileService.create(profile);
        }
        return mav;
    }

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() throws Exception{
        return "404";
    }


}
