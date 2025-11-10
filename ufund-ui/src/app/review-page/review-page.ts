import { Component, OnInit } from '@angular/core';
import { ReviewService, Review } from '../review-service';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reviews-page',
  templateUrl: './review-page.html',
  styleUrls: ['./review-page.css'],
  standalone: false
})
export class ReviewsPage implements OnInit {
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
    private router: Router
  ) {}

  ngOnInit(): void {
    const u = this.user.getCurrentUser();
    if (!u) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/needs' } });
      return;
    }
    this.form.userName = u; // will always create a review for the current user

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

  submit(): void {
    const name = this.form.userName;
    const text = this.form.reviewText;

    if (name == null) { return; }
    if (text == null) { return; }

    const n = name.trim();
    const t = text.trim();

    if (!n) { 
      alert('Please enter your name.');
      return;
    }
    if (!t) {
      alert('Please enter a review.');
      return;
    }

    const self = this;
    this.reviewService.addReview({ userName: n, reviewText: t, rating: this.form.rating })
      .subscribe(function(_) {
        self.form.reviewText = "";
        self.form.rating = 5;
        self.loadByRating();
      });
  }

  delete(id: number): void {
    const ok = window.confirm('Delete review?');
    if (!ok) {
      return;
    }

    const self = this;
    this.reviewService.deleteReview(id).subscribe(function(_) {
      self.loadByRating();
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
}
