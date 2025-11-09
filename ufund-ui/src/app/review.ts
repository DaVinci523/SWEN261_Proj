export interface Review {
  id: number;
  userName: string;
  reviewText: string;
  rating: number;
}

export interface CreateReview {
  userName: string;
  reviewText: string;
  rating: number;
}