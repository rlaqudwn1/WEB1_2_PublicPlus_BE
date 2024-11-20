package backend.dev.facility.service;

import backend.dev.movie.entity.Movie;
import backend.dev.movie.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FacilitySearchService {

    private final MovieRepository movieRepository;

    //
    @Autowired
    DataSource dataSource;
    @Autowired
    public FacilitySearchService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @BeforeAll
    public void init(){
        try(Connection conn = dataSource.getConnection()){
            ScriptUtils.executeSqlScript(conn ,new ClassPathResource("/db/h2/data.sql"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void facilitySearch(){
        List<Movie> in = movieRepository.findByTitleContaining("in");

        System.out.println("in = " + in);
    }

}
