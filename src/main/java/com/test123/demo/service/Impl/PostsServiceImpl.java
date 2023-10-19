package com.test123.demo.service.Impl;

import com.test123.demo.entity.Posts;
import com.test123.demo.entity.QPosts;
import com.test123.demo.entity.User;
import com.test123.demo.model.Posts.PostsCreate;
import com.test123.demo.model.Posts.PostsUpdate;
import com.test123.demo.repository.PostsRepository;
import com.test123.demo.repository.UserRepository;
import com.test123.demo.service.Impl.querydsl.QuerydslRepository;
import com.test123.demo.service.PostsService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostsServiceImpl implements PostsService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuerydslRepository querydsl;


    @Override
    public Option<Posts> getPostById(String id) {
        QPosts posts = QPosts.posts;
        return Option.of(querydsl.newQuery()
                .selectFrom(posts)
                .where(posts.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<Posts> getAllPosts() {
        return postsRepository.findAll();
    }

    @Override
    public List<Posts> getFilterPosts(int page, int size, String postId, String userId, String title, String content) {
        Specification<Posts> spec = Specification.where(null);

        if (postId != null){
            assert spec != null;
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("postId"), postId));
        }

        if (userId != null){
            assert spec != null;
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId"), userId));

        }

        if(title != null){
            assert spec != null;
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),"%"+title.toLowerCase()+"%"));
        }

        if(content != null){
            assert spec != null;
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("content")),"%"+content.toLowerCase()+"%"));

        }
        System.out.println("Page"+ page);
        System.out.println("Size"+ size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> postsPage = postsRepository.findAll(spec,pageable);
        postsRepository.count();
        return postsPage.getContent();
    }



    @Override
    public Option<Posts> createPost(PostsCreate creation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){

            String userId = authentication.getName();
            Optional<User> userOptional = userRepository.findById(userId);
            if (!userOptional.isPresent()) {
                return Option.none();
            }
            Posts newPosts = new Posts();
            newPosts.setUser(userOptional.get());
            newPosts.setTitle(creation.getTitle());
            newPosts.setContent(creation.getContent());
            newPosts.setCreateAt(LocalDateTime.now());
            newPosts.setStatus("1");

            newPosts = postsRepository.save(newPosts);
            return Option.of(newPosts);
        }
        return Option.none();
    }

    @Override
    @Transactional
    public void deletePost(String id) {
       Posts postsOption = postsRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found."));
        postsRepository.delete(postsOption);
    }

    @Override
    @Transactional
    public Option<Posts> updatePost(String id, PostsUpdate postsUpdate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){
            String currentUserId = authentication.getName();

            Option<Posts> optionalPost = postsRepository.findByIdAndUserId(id, currentUserId);

            if (!optionalPost.isEmpty()) {
                Posts updatePost = optionalPost.get();

                updatePost.setTitle(postsUpdate.getTitle());
                updatePost.setContent(postsUpdate.getContent());

                updatePost.setUpdateAt(LocalDateTime.now());

                postsRepository.save(updatePost);

                return Option.of(updatePost);
            }
        }

        return Option.none();
    }
}
