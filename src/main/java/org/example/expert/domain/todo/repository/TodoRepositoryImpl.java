package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchCond;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.*;
import static org.example.expert.domain.manager.entity.QManager.*;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

public class TodoRepositoryImpl implements TodoRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);

    }

    public Page<TodoSearchResponse> searchTodos(TodoSearchCond searchCond, Pageable pageable) {
        List<TodoSearchResponse> content = queryFactory.select(new QTodoSearchResponse(
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct()))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .leftJoin(manager.user, user)
                .where(
                        titleContain(searchCond.getTitle()),
                        managerNameContain(searchCond.getManagerName()),
                        createdAtGoe(searchCond.getStartDate()),
                        createdAtLoe(searchCond.getEndDate()))
                .groupBy(todo.id, todo.title)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(todo.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .where(titleContain(searchCond.getTitle()),
                        managerNameContain(searchCond.getManagerName()),
                        createdAtGoe(searchCond.getStartDate()),
                        createdAtLoe(searchCond.getEndDate()));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleContain(String title) {
        return StringUtils.hasText(title) ? todo.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression createdAtGoe(LocalDateTime startDate) {
        return startDate != null ? todo.createdAt.goe(startDate) : null;
    }
    private BooleanExpression createdAtLoe(LocalDateTime endDate) {
        return endDate != null ? todo.createdAt.loe(endDate) : null;
    }

    private BooleanExpression managerNameContain(String managerName){
        return StringUtils.hasText(managerName) ? manager.user.nickName.containsIgnoreCase(managerName) : null;
    }
}
