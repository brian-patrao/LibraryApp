class ReviewRequestModel {
    rating: number;
    bookId: number;
    reviewDescr?: string;

    constructor(rating: number, bookId: number, reviewDescription: string) {
        this.rating = rating;
        this.bookId = bookId;
        this.reviewDescr = reviewDescription;
    }
}

export default ReviewRequestModel;