import { useOktaAuth } from "@okta/okta-react";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import ShelfCurrentLoans from "../../models/ShelfCurrentLoans";
import { Spinner } from "../../utils/Spinner";

export const Loans = () => {
    const { authState } = useOktaAuth();
    const [httpError, setHttpError] = useState(null);

    // Current Loans
    const [shelfCurrentLoans, setShelfCurrentLoans] = useState<
        ShelfCurrentLoans[]
    >([]);
    const [isLoadingUserLoans, setIsLoadingUserLoans] = useState(true);

    useEffect(() => {
        const fetchUserCurrentLoans = async () => {
            if (authState && authState.isAuthenticated) {
                const url = `http://localhost:8080/api/checkout/getCheckedOut?userEmail=${authState.idToken?.claims.email}`;
                const shelfCurrentLoansResponse = await fetch(url);
                if (!shelfCurrentLoansResponse.ok) {
                    throw new Error("Something went wrong!");
                }
                const shelfCurrentLoansResponseJson =
                    await shelfCurrentLoansResponse.json();

                setShelfCurrentLoans(shelfCurrentLoansResponseJson);
            }
            setIsLoadingUserLoans(false);
        };
        fetchUserCurrentLoans().catch((error: any) => {
            setIsLoadingUserLoans(false);
            setHttpError(error.message);
        });
        window.scrollTo(0, 0);
    }, [authState]);

    if (isLoadingUserLoans) {
        return <Spinner />;
    }

    if (httpError) {
        return (
            <div className="container m-5">
                <p>{httpError}</p>
            </div>
        );
    }

    return (
        <div>
            {/* Desktop */}
            <div className="d-none d-lg-block mt-2">
                {shelfCurrentLoans.length > 0 ? (
                    <>
                        <h5>Currently Borrowed Books: </h5>

                        {shelfCurrentLoans.map((shelfCurrentLoan) => (
                            <div key={shelfCurrentLoan.book.id}>
                                <div className="row mt-3 mb-3">
                                    <div className="col-4 col-md-4 container">
                                        <img
                                            src={require("./../../Images/PublicImages/static-book-image.jpg")}
                                            width="226"
                                            height="349"
                                            alt="Book"
                                        />
                                    </div>
                                    Title: {shelfCurrentLoan.book.title}
                                    <div className="card col-3 col-md-3 container d-flex">
                                        <div className="card-body">
                                            <div className="mt-3">
                                                <h4>Borrow Status</h4>
                                                {shelfCurrentLoan.daysLeft >
                                                    0 && (
                                                    <p className="text-secondary">
                                                        Due in{" "}
                                                        {
                                                            shelfCurrentLoan.daysLeft
                                                        }{" "}
                                                        days.
                                                    </p>
                                                )}
                                                {shelfCurrentLoan.daysLeft ===
                                                    0 && (
                                                    <p className="text-success">
                                                        Due Today.
                                                    </p>
                                                )}
                                                {shelfCurrentLoan.daysLeft <
                                                    0 && (
                                                    <p className="text-danger">
                                                        Past due by{" "}
                                                        {
                                                            shelfCurrentLoan.daysLeft
                                                        }{" "}
                                                        days.
                                                    </p>
                                                )}
                                                <div className="list-group mt-3">
                                                    <Link
                                                        to={"search"}
                                                        className="list-group-item list-group-item-action"
                                                    >
                                                        Search more books?
                                                    </Link>
                                                </div>
                                            </div>
                                            <hr />
                                            <p className="mt-3">
                                                Review this book
                                            </p>
                                            <Link
                                                className="btn btn-primary"
                                                to={`/checkout/${shelfCurrentLoan.book.id}`}
                                            >
                                                Leave a review
                                            </Link>
                                        </div>
                                    </div>
                                </div>
                                <hr />
                            </div>
                        ))}
                    </>
                ) : (
                    <>
                        <h3 className="mt-3">Currently no loans</h3>
                        <Link className="btn btn-primary" to={`search`}>
                            Search for a new book
                        </Link>
                    </>
                )}
            </div>
        </div>
    );
};
