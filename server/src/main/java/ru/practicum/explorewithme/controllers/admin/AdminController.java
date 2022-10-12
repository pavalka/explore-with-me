package ru.practicum.explorewithme.controllers.admin;

import net.bytebuddy.asm.Advice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.dto.comments.CommentFullDto;
import ru.practicum.explorewithme.dto.compilations.CompilationDto;
import ru.practicum.explorewithme.dto.compilations.NewCompilationDto;
import ru.practicum.explorewithme.dto.events.AdminUpdateEventRequest;
import ru.practicum.explorewithme.dto.events.EventFullDto;
import ru.practicum.explorewithme.dto.users.NewUserRequest;
import ru.practicum.explorewithme.dto.users.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.entities.comments.CommentStatus;
import ru.practicum.explorewithme.entities.events.EventState;
import ru.practicum.explorewithme.services.categories.CategoryService;
import ru.practicum.explorewithme.services.comments.CommentFilterParam;
import ru.practicum.explorewithme.services.comments.CommentService;
import ru.practicum.explorewithme.services.compilations.CompilationService;
import ru.practicum.explorewithme.services.events.EventAdminFilterParam;
import ru.practicum.explorewithme.services.events.EventService;
import ru.practicum.explorewithme.services.users.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CommentService commentService;

    //-------------------------- User endpoints -------------------------------------

    @GetMapping(value = "/users", params = {"ids", "!from", "!size"})
    public Collection<UserDto> getUsersByIds(@RequestParam(name = "ids") List<Long> ids) {
        return userService.getUsers(ids);
    }

    @GetMapping(value = "/users", params = {"!ids"})
    public Collection<UserDto> getAllUsersPageable(@RequestParam(name = "from", defaultValue = "0")
                                                   @PositiveOrZero int from,
                                                   @RequestParam(name = "size", defaultValue = "10")
                                                   @Positive int size) {
        return userService.getUsers(from, size);
    }

    @PostMapping("/users")
    public UserDto createNewUser(@RequestBody @Valid NewUserRequest user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable(name = "userId") long userId) {
        userService.deleteUser(userId);
    }

    //-------------------------- Category endpoints ---------------------------------

    @PatchMapping("/categories")
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto createNewCategory(@RequestBody @Valid NewCategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable(name = "catId") long categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    //-------------------------- Event endpoints -------------------------------------

    @GetMapping("/events")
    public Collection<EventFullDto> getEvents(@RequestParam(name = "users", required = false) List<Long> userIds,
                                              @RequestParam(name = "states", required = false) List<EventState> states,
                                              @RequestParam(name = "categories", required = false) List<Long> categories,
                                              @RequestParam(name = "rangeStart", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                          LocalDateTime rangeStart,
                                              @RequestParam(name = "rangeEnd", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                          LocalDateTime rangeEnd,
                                              @RequestParam(name = "from", defaultValue = "0") long from,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        var filterParam = EventAdminFilterParam.builder()
                .userIds(userIds)
                .states(states)
                .categoryIds(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return eventService.getAllEventsFilteredForAdmin(filterParam);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "eventId") long eventId,
                                    @RequestBody @Valid AdminUpdateEventRequest eventDto) {
        return eventService.updateEventForAdmin(eventId, eventDto);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable(name = "eventId") long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable(name = "eventId") long eventId) {
        return eventService.rejectEvent(eventId);
    }

    //-------------------------- Compilation endpoints -------------------------------------

    @PostMapping("/compilations")
    public CompilationDto createNewCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        return compilationService.createNewCompilation(compilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable(name = "compId") long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable(name = "compId") long compilationId,
                                           @PathVariable(name = "eventId") long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable(name = "compId") long compilationId,
                                      @PathVariable(name = "eventId") long eventId) {
        compilationService.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable(name = "compId") long compilationId) {
        compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable(name = "compId") long compilationId) {
        compilationService.pinCompilation(compilationId);
    }

    //-------------------------- Comment endpoints -------------------------------------

    @PatchMapping("/comments/{comId}/moderate")
    public void moderateComment(@PathVariable(name = "comId") long commentId) {
        commentService.setCommentStatus(commentId, CommentStatus.MODERATED);
    }

    @PatchMapping("/comments/{comId}/reject")
    public void rejectComment(@PathVariable(name = "comId") long commentId) {
        commentService.setCommentStatus(commentId, CommentStatus.REJECTED);
    }

    @GetMapping("/comments")
    public Collection<CommentFullDto> getCommentsFiltered(@RequestParam(name = "events", required = false)
                                                                      List<Long> eventIds,
                                                          @RequestParam(name = "authors", required = false)
                                                                  List<Long> authorIds,
                                                          @RequestParam(name = "statuses", required = false)
                                                                      List<CommentStatus> statuses,
                                                          @RequestParam(name = "rangeStart", required = false)
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                                      LocalDateTime rangeStart,
                                                          @RequestParam(name = "rangeEnd", required = false)
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                                      LocalDateTime rangeEnd,
                                                          @RequestParam(name = "edited", defaultValue = "false")
                                                                      Boolean edited,
                                                          @RequestParam(name = "from", defaultValue = "0")
                                                              @PositiveOrZero long from,
                                                          @RequestParam(name = "size", defaultValue = "10")
                                                              @Positive int size) {
        var filterParam = CommentFilterParam.builder()
                .eventIds(eventIds)
                .authorIds(authorIds)
                .statuses(statuses)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .edited(edited)
                .from(from)
                .size(size)
                .build();

        return commentService.getCommentsFiltered(filterParam);
    }

    @GetMapping("/comments/{comId}")
    public CommentFullDto getCommentById(@PathVariable(name = "comId") long commentId) {
        return commentService.getCommentForAdmin(commentId);
    }

    @DeleteMapping("/comments")
    public void deleteAllComments() {
        commentService.deleteAllComments();
    }

    @DeleteMapping("/comments/{comId}")
    public void deleteCommentById(@PathVariable(name = "comId") long commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}
