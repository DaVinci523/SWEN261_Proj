import { Component, OnInit } from '@angular/core';
import { ReviewService, Review } from '../review-service';
import { User } from '../user';
import { Router } from '@angular/router';
import { ModalService } from '../modal-service';

@Component({
  selector: 'app-reviews-page',
  templateUrl: './review-page.html',
  styleUrls: ['./review-page.css'],
  standalone: false
})
export class ReviewsPage implements OnInit {
   private reviewsChannel?: BroadcastChannel;


  private messageHandler = (event: MessageEvent) => {
    console.log("I GOT A MESSAGE: " + event);
    if (event.data && event.data.startsWith('review-updated')) {
      this.loadByRating();
      console.log("Reviews updated");
    }
  };

  reviews: Review[] = [];
  avgRating = 0;
  dummyTracker = 0; // angular does not like all the stars being individual elements with the same key, so this will give them different keys

  // filter and form
  filterRating: number | null = null;
  form = { userName: '', reviewText: '', rating: 5 };
  Math: any;


  constructor(
    private reviewService: ReviewService, 
    public user: User, 
    private router: Router,
    private modal: ModalService
  ) {}

  ngOnInit(): void {
    const u = this.user.getCurrentUser();
    if (!u) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/needs' } });
      return;
    }
    this.form.userName = u; // will always create a review for the current user

    window.addEventListener('message', this.messageHandler); // listener for a message

    if ('BroadcastChannel' in window) { // connect broadcast message
      console.log("BRODCAST IN CHANNEL")
      this.reviewsChannel = new BroadcastChannel('reviews');
      this.reviewsChannel.onmessage = (ev: MessageEvent) => {
        if (ev.data === 'review-updated') {
          console.log('ReviewPage received review-updated via BroadcastChannel', ev.data);
          this.loadByRating();
        }
      };
      console.log('NeedsCupboard BroadcastChannel listener attached');
    }

    this.loadAll();
  }

  updateRating() { // make sure the input is always between 1 and 5
    if(this.form.rating < 1){
      this.form.rating = 1;
    }
    else if(this.form.rating > 5){
      this.form.rating = 5;
    }
  }


  loadAll(): void {
    const self = this;
    this.reviewService.getReviews().subscribe(function(list) {
      console.log(list);
      self.onLoaded(list);
    });
  }

  loadByRating(): void {
    if (this.filterRating == null) {
      this.loadAll();
      return;
    }
    const self = this;
    this.reviewService.getReviewsByRating(this.filterRating).subscribe(function(list) {
      console.log(list);
      self.onLoaded(list);
    });
  }

  private onLoaded(list: Review[] | null | undefined): void {
    if (list) {
      this.reviews = list;
    } else {
      this.reviews = [];
    }

    if (this.reviews.length > 0) {
      let sum = 0;
      let i = 0;
      while (i < this.reviews.length) {
        let r = this.reviews[i].rating;
        if (typeof r != 'number') {
          r = 0;
        }
        sum = sum + r;
        i = i + 1;
      }
      const avg = sum / this.reviews.length;
      this.avgRating = Math.round(avg * 10) / 10;
    } else {
      this.avgRating = 0;
    }
  }

  create(): void {
    const name = this.form.userName;
    const text = this.form.reviewText;

    if (name == null) { return; }
    if (text == null) { return; }

    const n = name.trim();
    const t = text.trim();

    if (!n) { 
      this.modal.infoDialog('Error', 'Please enter your name.', 'OK').subscribe(() => {});
      return;
    }
    if (!t) {
      this.modal.infoDialog('Error', 'Please enter a review.', 'OK').subscribe(() => {});
      return;
    }

    const self = this;
    this.reviewService.addReview({ userName: n, reviewText: t, rating: this.form.rating })
      .subscribe(function(_) {
        self.form.reviewText = "";
        self.form.rating = 5;
        self.loadByRating();
        self.notifyReviewModified();
      });

  }

  delete(id: number): void {
    this.modal.confirmDialog('Review', 'Delete review?', 'Yes', 'No')
    .subscribe(ok => {
      if (ok) {
        const self = this;
        this.reviewService.deleteReview(id).subscribe(function(_) {
          self.loadByRating();
          self.notifyReviewModified();
        });
      }
    });
  }

  clearFilter(): void {
    this.filterRating = null;
    this.loadAll();
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

  /**
   * Notify the application when a review is modified
   */
  private notifyReviewModified(): void {
    // BroadcastChannel for immediate cross-tab messaging
    try {
      if ('BroadcastChannel' in window) {
        console.log("BROADCASTED")
        const bc = new BroadcastChannel('reviews');
        bc.postMessage('review-updated');
        bc.close();
      }
    } catch {}
  }
}
