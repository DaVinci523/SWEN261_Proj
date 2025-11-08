import { Component } from '@angular/core';
import { Review, ReviewService } from '../review-service';

@Component({
  selector: 'app-reviews-card',
  standalone: false,
  templateUrl: './reviews-card.html',
  styleUrl: './reviews-card.css'
})
export class ReviewsCard {
  allReviews: Review[] = [];
  displayedReviews: Review[] = [];
  avgRating = 0;
  dummyTracker = 0; // angular does not like all the stars being individual elements with the same key, so this will give them different keys

    
  constructor(
    private reviewService: ReviewService, 
    // private router: Router
  ) {}

  ngOnInit(): void {

    this.loadAll();
  }

  loadAll(): void {
    const self = this;
    this.reviewService.getReviews().subscribe((list: Review[]) => {
      this.allReviews = list
      let randNum1 = Math.floor(Math.random() * list.length); // generate random numbers for indeces of random reviews
      let randNum2 = Math.floor(Math.random() * list.length);
      
      this.displayedReviews.push(list[randNum1]);
      this.displayedReviews.push(list[randNum2]);

    });
  }

  /**
   * Pulls up two random reviews to display
   */
  refreshReviews(): void {
    let randNum1 = Math.floor(Math.random() * this.allReviews.length); // generate random numbers for indeces of random reviews
    let randNum2 = Math.floor(Math.random() * this.allReviews.length);
      
    this.displayedReviews.push(this.allReviews[randNum1]);
    this.displayedReviews.push(this.allReviews[randNum2]);
  }


}
