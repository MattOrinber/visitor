/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.MessageContent;
import org.visitor.appportal.repository.MessageContentRepository;

@Controller
@RequestMapping("/domain/messagecontent/")
public class MessageContentControllerWithPathVariable {
    @Autowired
    private MessageContentRepository messageContentRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a MessageContent via the {@link MessageContentFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public MessageContent getMessageContent(@PathVariable("pk") Long pk) {
        return messageContentRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute MessageContent messageContent) {
        return "domain/messagecontent/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/messagecontent/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute MessageContent messageContent, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }

        messageContentRepository.save(messageContent);
        return "redirect:/domain/messagecontent/show/" + messageContent.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/messagecontent/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute MessageContent messageContent) {
        messageContentRepository.delete(messageContent);
        return "redirect:/domain/messagecontent/search";
    }

}