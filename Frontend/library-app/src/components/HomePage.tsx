import React from "react";
import { SearchBooksHome } from "./HomePage/SearchBooksHome";
import { TopBooks } from "./HomePage/TopBooks";

export const HomePage = () => {
    return (
        <>
            <SearchBooksHome />
            <TopBooks />
        </>
    );
};
