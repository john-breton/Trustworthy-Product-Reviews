# Trustworthy Product Reviews
#### Group Members: Mariam Almalki, John Breton, Cameron Davis, Ryan Godfrey, Sarah Jaber

##### Tools

- [IntelliJ IDEA](https://www.jetbrains.com/idea/download)
- [Java 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=hotspot)
- [Maven](https://maven.apache.org/download.cgi)
 
## Project Description
Users review Products. These products could be identified by a link to the website where they are listed. A review consists of a star-rating and some text. Users can also "follow" other users whose reviews they find valuable. A user can then list products for a given category by average rating, or the average rating of only the users they follow. A user can also list the users whose ratings are the most "similar" to their own using the Jaccard distance (google it!). Product reviews can also be ranked according to the similarity score of the people who reviewed them. Users can also list the most followed users. The degree of separation (see Kevin Bacon!) according to the "follow" link can also be displayed next to each reviewer (the assumption is that the "closer" a user is to you, the more trustworthy he/she should be to you).

## Milestone Objectives
### Milestone 2
 - [ ] Populate the backend product database upon project boot
 - [ ] Implement and display Jaccard distance calculations
 - [ ] Add the ability to show the average rating for the users a given user is following
 - [ ] Implement and display degrees of separation calculations
 - [ ] Add the ability to rank product reviews based on the similarity score of the people that have reviewed them
 - [ ] Add the ability to list products by category and to sort them by average rating
 - [ ] Add a full site navigation bar that can reliably bring the user to any relevant webpage.

## Current ER Diagram

<p style="text-align:right">
<img src="documentation\ERDiagram.png" alt="ER Diagram">
</p>
Date: March 10th, 2022

## Current UML Class Diagram of Models

<p style="text-align:right">
<img src="documentation\ClassDiagram.png" alt="Class Diagram">
</p>
Date: March 10th, 2022
