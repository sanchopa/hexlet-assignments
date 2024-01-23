package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping(path = "")
    List<PostDTO> posts() {
        var posts = postRepository.findAll();
        return posts.stream().map(this::getPostDTO).toList();
    }

    @GetMapping(path = "/{id}")
    PostDTO post(@PathVariable long id) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        return getPostDTO(post);
    }

    private PostDTO getPostDTO(Post post) {
        var postDTO = new PostDTO();
        long postId = post.getId();
        postDTO.setId(postId);
        postDTO.setBody(post.getBody());
        postDTO.setTitle(post.getTitle());
        postDTO.setComments(getListCommentDTO(postId));
        return postDTO;
    }

    private List<CommentDTO> getListCommentDTO(long id) {
        var comments = commentRepository.findByPostId(id);
        return comments.stream().map(this::getCommentDTO).toList();
    }

    private CommentDTO getCommentDTO(Comment comment) {
        var commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setBody(comment.getBody());
        return commentDTO;
    }
}
