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
    while(this.displayedReviews.length > 0){ // clear the display
      this.displayedReviews.pop();
    }

    // add to the display
    let randNum1 = Math.floor(Math.random() * this.allReviews.length); // generate random numbers for indeces of random reviews
    let randNum2 = Math.floor(Math.random() * this.allReviews.length);
      
    this.displayedReviews.push(this.allReviews[randNum1]);
    this.displayedReviews.push(this.allReviews[randNum2]);
  }

  getStars(n: number): number[] { // returns an array of the size of the given rating, a helper function for the @for loop in review-page.html
    let count = n;
    if (count < 0) { count = 0; }
    if (count > 5) { count = 5; }

    const arr: number[] = [];
    let i = 0;
    while (i < count) {
      arr.push(this.dummyTracker);
      this.dummyTracker++;
      i = i + 1;
    }
    return arr;
  }
}
