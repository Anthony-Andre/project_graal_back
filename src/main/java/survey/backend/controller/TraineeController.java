package survey.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import survey.backend.dto.TraineeDto;
import survey.backend.entities.Trainee;
import survey.backend.error.BadRequestError;
import survey.backend.error.NoDataFoundError;
import survey.backend.service.TraineeService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/trainee")
public class TraineeController {

    @Autowired // DI (dependency Injection)
    private TraineeService traineeService;

    /**
     * list of trainees
     * route: /api/trainee
     *
     * @return list of trainees
     */
    @GetMapping
    public Iterable<Trainee> getAll() {
        return traineeService.findAll();
    }

    /**
     * a trainee by its id
     * route: /api/trainee/{id}
     *
     * @param id
     * @return a trainee
     */
    @GetMapping("{id}")
    public Trainee getById(@PathVariable("id") int id) {
        Optional<Trainee> optTrainee = traineeService.findById(id);
        if (optTrainee.isPresent()) {
            return optTrainee.get();
        } else {
            throw NoDataFoundError.withId("Trainee", id);
        }
    }

    /**
     * search trainees with criteria
     * route: /api/trainee/search?fn=some_firstname&ln=some_lastname
     *
     * @param firstname (optional)
     * @param lastname  (optional)
     * @return trainees corresponding
     */
//    @GetMapping("search")
//    public Iterable<Trainee> search(
//            @RequestParam(name="ln", required = false) String lastname,
//            @RequestParam(name="fn", required = false) String firstname
//    ){
//        // TODO: return 400 BAD REQUEST if both params are null
//        if(lastname == null & firstname == null){
//            throw BadRequestError.withNoArgs("Salut les petits potes, il manque quelque chose");
//        }
//        return traineeService.search(lastname, firstname);
//    }
    @GetMapping("search")
    public Iterable<Trainee> search(
            @RequestParam(name = "ln", required = false) String lastname,
            @RequestParam(name = "fn", required = false) String firstname
    ) {
        int size = 0;

        if (lastname == null && firstname == null) {
            throw new BadRequestError("search with no args not permitted");
        }

        Iterable<Trainee> iTrainees = traineeService.search(lastname, firstname);
        if (iTrainees instanceof Collection<Trainee>) {
            size = ((Collection<Trainee>) iTrainees).size();
        }

        if (size == 0) {
            throw NoDataFoundError.noResults("Trainee search", lastname + " " + firstname);
        }
        return iTrainees;
    }


    /**
     * add new trainee with data in json body
     * route: POST /api/trainee
     *
     * @param traineeDto
     * @return trainee added/completed
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trainee add(@Valid @RequestBody TraineeDto traineeDto) {
        return traineeService.add(traineeDto);
    }

    /**
     * update trainee with data in json body
     * route: PUT /api/trainee
     *
     * @param traineeDto
     * @return
     */
    @PutMapping
    public Trainee update(@RequestBody TraineeDto traineeDto) {
        return traineeService.update(traineeDto)
                .orElseThrow(() -> NoDataFoundError.withId("Trainee", Math.toIntExact(traineeDto.getId())));
    }

    /**
     * delete trainee with its id
     * route: DELETE /api/trainee/{id}
     *
     * @param id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        if (!traineeService.delete(id)) {
            throw NoDataFoundError.withId("Trainee", id);
        }
    }
}