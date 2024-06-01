package com.example.jp.services.Topics;

import com.example.jp.model.Topics.Link;
import com.example.jp.repositories.Topics.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    public List<Link> getLinksByTopic(Long topicId) {
        return linkRepository.findByTopic_Id(topicId);
    }

    public Optional<Link> getLinkById(Long id) {
        return linkRepository.findById(id);
    }

    public Link saveLink(Link link) {
        return linkRepository.save(link);
    }

    public void deleteLink(Long id) {
        linkRepository.deleteById(id);
    }

    public Link updateLink(Long id, Link updatedLink) {
        Optional<Link> optionalLink = linkRepository.findById(id);
        if (optionalLink.isPresent()) {
            Link existingLink = optionalLink.get();
            existingLink.setTitle(updatedLink.getTitle());
            existingLink.setDescription(updatedLink.getDescription());
            existingLink.setLink(updatedLink.getLink());
            existingLink.setTopic(updatedLink.getTopic());
            return linkRepository.save(existingLink);
        }
        return null;
    }
}
