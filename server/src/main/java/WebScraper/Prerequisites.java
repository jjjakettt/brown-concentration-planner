package WebScraper;

import WebScraper.Certificate.TrustAllCerts;
import java.util.List;
import java.util.ArrayList;

public class Prerequisites {
    private List<List<String>> requiredCourses; // Each inner list represents OR conditions within AND groups
    
    public Prerequisites() {
        this.requiredCourses = new ArrayList<>();
    }
    
    public void addPrerequisiteGroup(List<String> group) {
        requiredCourses.add(group);
    }
    
    public List<List<String>> getRequiredCourses() {
        return requiredCourses;
    }
    
    public boolean areSatisfied(List<String> completedCourses) {
        if (requiredCourses.isEmpty()) {
            return true; // No prerequisites
        }
        
        // Check if all AND groups are satisfied
        return requiredCourses.stream().allMatch(orGroup -> 
            // Within each AND group, at least one OR option must be satisfied
            orGroup.stream().anyMatch(prereq -> 
                completedCourses.contains(prereq.trim().toUpperCase())
            )
        );
    }
    
    public List<String> getMissingPrerequisites(List<String> completedCourses) {
        List<String> missing = new ArrayList<>();
        
        for (List<String> orGroup : requiredCourses) {
            if (!orGroup.stream().anyMatch(prereq -> 
                    completedCourses.contains(prereq.trim().toUpperCase()))) {
                missing.add(String.join(" or ", orGroup));
            }
        }
        
        return missing;
    }
}