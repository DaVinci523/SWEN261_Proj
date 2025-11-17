import { Component } from '@angular/core';
import { Review, ReviewService } from '../review-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reviews-card',
  standalone: false,
  templateUrl: './reviews-card.html',
  styleUrl: './reviews-card.css'
})
export class ReviewsCard {
  private reviewsChannel?: BroadcastChannel;

  private messageHandler = (event: MessageEvent) => {
    console.log("I GOT A MESSAGE: " + event);
    if (event.data && event.data.startsWith('review-updated')) {
      this.loadAll();
      console.log("Reviews updated");
    }
  };

  allReviews: Review[] = [];
  displayedReviews: Review[] = [];
  avgRating = 0;
  dummyTracker = 0; // angular does not like all the stars being individual elements with the same key, so this will give them different keys
    
  constructor(
    private reviewService: ReviewService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    window.addEventListener('message', this.messageHandler); // listener for a message

    if ('BroadcastChannel' in window) { // connect broadcast message
      console.log("BRODCAST IN CHANNEL")
      this.reviewsChannel = new BroadcastChannel('reviews');
      this.reviewsChannel.onmessage = (ev: MessageEvent) => {
        if (ev.data === 'review-updated') {
          console.log('ReviewPage received review-updated via BroadcastChannel', ev.data);
          this.loadAll();
        }
      };
      console.log('NeedsCupboard BroadcastChannel listener attached');
    }

    this.loadAll();
  }

  loadAll(): void {
    
    const self = this;
    this.reviewService.getReviews().subscribe((list: Review[]) => {
      if(list.length < 2){ // do nothing if there are not 2 or more reviews
        console.log("Break")
        return;
      }
      this.allReviews = list;
      this.displayedReviews = [];
      
      
      let randNum1 = Math.floor(Math.random() * list.length); // generate random numbers for indeces of random reviews
      let randNum2 = Math.floor(Math.random() * list.length);
      
      while(randNum2 == randNum1){ // keep re-rolling if the ids are the same (avoid repeating reviews)
        randNum2 = Math.floor(Math.random() * list.length);
      }

      this.displayedReviews.push(list[randNum1]);
      this.displayedReviews.push(list[randNum2]);

    });
  }

  /**
   * Pulls up two random reviews to display
   */
  refreshReviews(): void {
    if(this.allReviews.length < 2){ // do nothing if there are not 2 or more reviews
      return;
    }

    let randNum1 = Math.floor(Math.random() * this.allReviews.length); // generate random numbers for indeces of random reviews
    let randNum2 = Math.floor(Math.random() * this.allReviews.length);
    
    // avoid repeat reviews at the same time
    while(randNum2 == randNum1){
      randNum2 = Math.floor(Math.random() * this.allReviews.length);
    }

    while(this.displayedReviews.length > 0){ // clear the display
      this.displayedReviews.pop();
    }

    this.displayedReviews.push(this.allReviews[randNum1]); // add to the display
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

  // Navigate to the reviews page
  goToReviews(): void {
    this.router.navigate(['/reviews']);
  }
}
