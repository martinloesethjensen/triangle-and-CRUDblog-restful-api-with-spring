package api.rest.martin.restfulapiwithspring.Controller;

import api.rest.martin.restfulapiwithspring.Model.Comment;
import api.rest.martin.restfulapiwithspring.Model.Post;
import api.rest.martin.restfulapiwithspring.Repositories.CommentRepo;
import api.rest.martin.restfulapiwithspring.Repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostRestController {
	@Autowired
	PostRepo postRepo;
	@Autowired
	CommentRepo commentRepo;

	@GetMapping("/api/post/{id}")
	public ResponseEntity<Post> getPost(@PathVariable("id") Long id) {
		if (!postRepo.existsById(id)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(postRepo.findPostById(id), HttpStatus.OK);
	}

	@GetMapping("/api/post/create")
	public ResponseEntity<Post> createPost(
			@RequestParam(defaultValue = "{{data}}") String data,
			@RequestParam(defaultValue = "{{author}}") String author
	)
	{
		return new ResponseEntity<>(postRepo.save(new Post(data, author)), HttpStatus.OK);
	}

	@GetMapping("/api/post/delete/{id}")
	public ResponseEntity<String> deletePost(@PathVariable("id") Long post_Id) {
		if (!postRepo.existsById(post_Id)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		Post post = postRepo.findPostById(post_Id);

		// Delete Comments related to the post before deleting the post
		for (Comment comment : post.getComments()) {
			commentRepo.delete(comment);
		}

		postRepo.delete(post);

		return new ResponseEntity<>("Deleted post with id: " + post_Id, HttpStatus.OK);
	}

	@GetMapping("/api/post/update/{id}")
	public ResponseEntity<Post> updatePost(@PathVariable("id") Long id,
	                                       @RequestParam(defaultValue = "{{data}}") String data,
	                                       @RequestParam(defaultValue = "{{author}}") String author)
	{
		if (!postRepo.existsById(id)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		Post post = postRepo.findPostById(id);
		post.setDate();
		post.setData(data);
		post.setAuthor(author);

		return new ResponseEntity<>(postRepo.save(post), HttpStatus.OK);
	}

}
