package br.com.alura.AluraFake.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT MAX(t.order) FROM Task t WHERE t.course.id = :courseId")
    Optional<Integer> findMaxOrderByCourseId(@Param("courseId") Long courseId);

    @Modifying
    @Query("""
                update Task t
                set t.order = t.order + 1000
                where t.course.id = :courseId
                and t.order >= :fromOrder
            """)
    void shiftOrderTemporarily(Long courseId, int fromOrder);

    @Modifying
    @Query("""
                update Task t
                set t.order = t.order - 999
                where t.course.id = :courseId
                and t.order >= :fromOrder + 1000
            """)
    void normalizeOrder(Long courseId, int fromOrder);


    @Query("""
                select t.type
                from Task t
                where t.course.id = :courseId
            """)
    List<Type> findTaskTypesByCourseId(Long courseId);

    @Query("""
                select t.order
                from Task t
                where t.course.id = :courseId
                order by t.order
            """)
    List<Integer> findOrdersByCourseId(Long courseId);

    boolean existsByCourseIdAndStatement(Long courseId, String statement);

    long countByCourseId(Long courseId);
}
