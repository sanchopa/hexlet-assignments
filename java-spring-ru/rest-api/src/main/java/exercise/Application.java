package exercise;

import exercise.model.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class Application {

    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        return ResponseEntity.of(posts.stream().filter(p -> p.getSlug().equals(id)).findFirst());
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<Post>> getUsersPost(@PathVariable int id) {
        return ResponseEntity.ok(posts.stream().filter(p -> p.getUserId() == id).toList());
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createUsersPost(@PathVariable int id, @RequestBody Post post) {
        var newPost = new Post();
        newPost.setUserId(id);
        newPost.setSlug(post.getSlug());
        newPost.setTitle(post.getTitle());
        newPost.setBody(post.getBody());
        posts.add(newPost);
        return newPost;
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody Post post) {
        var postIndex = -1;
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getSlug().equals(id)) {
                postIndex = i;
                break;
            }
        }
        if(postIndex>=0) {
            posts.set(postIndex, post);
            return ResponseEntity.ok().body(post);
        } else  {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(post);
        }
    }

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getSlug().equals(id));
    }
}
